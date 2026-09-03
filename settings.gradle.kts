pluginManagement {
    repositories {
//        google {
//            content {
//                includeGroupByRegex("com\\.android.*")
//                includeGroupByRegex("com\\.google.*")
//                includeGroupByRegex("androidx.*")
//            }
//        }
//        mavenCentral()
//        maven("https://jitpack.io")
//        gradlePluginPortal()
        maven("https://maven.myket.ir")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
//        google()
//        maven("https://jitpack.io")
//        mavenCentral()
        maven("https://maven.myket.ir")

        flatDir {
            dirs("libs")
        }
    }

}

rootProject.name = "TotanPay"
include(":app")
 