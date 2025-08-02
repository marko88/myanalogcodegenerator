package myanalogcodegenerator.parser

import kotlinx.coroutines.*
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds.*
import java.nio.file.WatchEvent

class SourceWatcher(
    private val folder: Path,
    private val debounceMillis: Long = 0,
    private val onFileChanged: (File, WatchEvent.Kind<out Any>) -> Unit
) {
    private val watcher = FileSystems.getDefault().newWatchService()
    private val pending = mutableMapOf<Path, Job>()
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        registerAll(folder)
        startWatching()
    }

    private fun registerAll(root: Path) {
        Files.walk(root).filter { Files.isDirectory(it) }.forEach {
            it.register(watcher, ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE)
        }
    }

    private fun startWatching() {
        scope.launch {
            while (true) {
                val key = watcher.take()
                for (event in key.pollEvents()) {
                    val changed = (key.watchable() as Path).resolve(event.context() as Path)
                    val kind = event.kind()

                    if (changed.toString().endsWith(".kt")) {
                        // Cancel existing debounce timer for this file
                        pending[changed]?.cancel()

                        // Start new debounce timer
                        pending[changed] = launch {
                            delay(debounceMillis)
                            pending.remove(changed)
                            onFileChanged(changed.toFile(), kind)
                        }
                    }
                }
                key.reset()
            }
        }
    }

    fun stop() {
        watcher.close()
        scope.cancel()
    }
}