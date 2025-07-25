package myanalogcodegenerator.domain.model

import domain.model.DataFlowSemantics.*
import domain.model.DependencyType
import myanalogcodegenerator.dsl.architecture

/* ─────────────────────────  VINYL APP  ────────────────────────── */
val VinylArchitecture = architecture("VinylApp") {

    /* ─────────── Presentation layer ─────────── */

    layer("presentation") {

        /* Views */
        view("SplashScreen") {
            func("navigateToNext", "Unit", semantics = Event)
            dependsOn("SplashPresenter")
        }
        view("CreateUserScreen") {
            func("onCreateClick", "Unit", semantics = Command)
            attr("username", "String", reactive = true, semantics = State)
            dependsOn("CreateUserPresenter")
        }
        view("LoginScreen") {
            func("onLoginClick", "Unit", semantics = Command)
            attr("user", "User?", reactive = true, semantics = State)
            dependsOn("LoginPresenter")
        }
        view("MyCollectionScreen") {
            func(
                "onVinylSelected",
                "Unit",
                params = listOf("vinylId" to "String"),
                semantics = Event
            )
            attr("collection", "List<Vinyl>", reactive = true, semantics = State)
            dependsOn("CollectionPresenter", DependencyType.USES)
        }
        view("VinylDetailsScreen") {
            attr("vinyl", "Vinyl", reactive = true, semantics = State)
            dependsOn("VinylDetailsPresenter")
        }
        view("UserDetailsScreen") {
            attr("user", "User", reactive = true, semantics = State)
            dependsOn("UserDetailsPresenter", DependencyType.USES)
        }

        /* Presenters */
        presenter("SplashPresenter") {
            func("handleStartup", "Unit", semantics = Command)
            dependsOn("CreateUserViewModel")       // vm1
        }
        presenter("CreateUserPresenter") {
            func("submitUser", "Unit", semantics = Command)
            dependsOn("CreateUserViewModel")
        }
        presenter("LoginPresenter") {
            func("performLogin", "Unit", semantics = Command)
            dependsOn("LoginViewModel")
        }
        presenter("CollectionPresenter") {
            func("showCollection", "Unit", semantics = Event)
            dependsOn("CollectionViewModel")
        }
        presenter("VinylDetailsPresenter") {
            func("renderDetails", "Unit", semantics = Event)
            dependsOn("VinylDetailsViewModel")
        }
        presenter("UserDetailsPresenter") {
            func("renderUser", "Unit", semantics = Event)
            /* (no VM yet) */
        }

        /* ViewModels */
        viewModel("CreateUserViewModel") {
            func("createUser", "Unit", semantics = Command)
        }
        viewModel("LoginViewModel") {
            func("login", "Unit", semantics = Command)
            dependsOn("UserRepository")            // repo1
        }
        viewModel("CollectionViewModel") {
            func("loadCollection", "Unit", semantics = Command)
            attr("collection", "Flow<List<Vinyl>>", reactive = true, semantics = State)
            dependsOn("GetCollectionUseCase")
        }
        viewModel("VinylDetailsViewModel") {
            func("loadVinylDetails", "Unit", semantics = Command)
            attr("vinyl", "Flow<Vinyl>", reactive = true, semantics = State)
            dependsOn("GetVinylDetailsUseCase")
        }
    }

    /* ─────────── Domain layer ─────────── */

    layer("domain") {

        useCase("GetCollectionUseCase") {
            func("execute", "Flow<List<Vinyl>>", semantics = Response)
            dependsOn("VinylRepository")
        }
        useCase("GetVinylDetailsUseCase") {
            func("execute", "Flow<Vinyl>", semantics = Response)
            dependsOn("VinylRepository")
        }
    }

    /* ─────────── Data layer ─────────── */

    layer("data") {

        repository("UserRepository") {
            func("login", "Flow<User>", semantics = Response)
        }
        repository("VinylRepository") {
            func("getCollection", "Flow<List<Vinyl>>", semantics = Response)
            func("getVinylDetails", "Flow<Vinyl>", semantics = Response)
            dependsOn("DiscogsServiceAPI", DependencyType.NETWORK_CALL)
            dependsOn("JunoServiceAPI", DependencyType.NETWORK_CALL)
        }

        api("DiscogsServiceAPI") {
            func("searchVinyl", "ApiResponse", semantics = Request)
        }
        api("JunoServiceAPI") {
            func("fetchDetails", "ApiResponse", semantics = Request)
        }
    }

    /* ─────────── Cross-layer data-flows ─────────── */

    flow {
        /* Commands issued by views */
        "CreateUserScreen#onCreateClick" commandTo "CreateUserViewModel#createUser"
        "LoginScreen#onLoginClick" commandTo "LoginViewModel#login"
        "MyCollectionScreen#onVinylSelected" eventTo "VinylDetailsViewModel#loadVinylDetails"

        /* ViewModel → View state streams */
        "LoginViewModel#login" responseTo "LoginScreen#user"
        "CollectionViewModel#collection" streamTo "MyCollectionScreen#collection"
        "VinylDetailsViewModel#vinyl" streamTo "VinylDetailsScreen#vinyl"

        /* UseCase → ViewModel responses */
        "GetCollectionUseCase#execute" responseTo "CollectionViewModel#loadCollection"
        "GetVinylDetailsUseCase#execute" responseTo "VinylDetailsViewModel#loadVinylDetails"
    }
}

