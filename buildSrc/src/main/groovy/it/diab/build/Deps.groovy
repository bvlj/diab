/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */

package it.diab.build

class Deps {

    class AndroidX {
        public static final String appCompat = "androidx.appcompat:appcompat:1.1.0"

        public static final String constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta3"

        public static final String paging = "androidx.paging:paging-runtime:2.1.0"

        public static final String preference = "androidx.preference:preference:1.1.0"

        public static final String workManager = "androidx.work:work-runtime-ktx:2.2.0"

        class Lifecycle {
            private static final String VERSION = "2.2.0-rc02"

            public static final String compiler = "androidx.lifecycle:lifecycle-compiler:$VERSION"
            public static final String extensions = "androidx.lifecycle:lifecycle-extensions:$VERSION"
            public static final String viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$VERSION"
        }

        class Room {
            private static final String VERSION = "2.2.1"

            public static final String compiler = "androidx.room:room-compiler:$VERSION"
            public static final String ktx = "androidx.room:room-ktx:$VERSION"
            public static final String runtime = "androidx.room:room-runtime:$VERSION"
        }

        class Test {

            public static final String core = 'androidx.arch.core:core-testing:2.0.1'

            class Espresso {
                private static final String VERSION = "3.2.0"

                public static final String core = "androidx.test.espresso:espresso-core:$VERSION"
                public static final String contrib = "androidx.test.espresso:espresso-contrib:$VERSION"
                public static final String intents = "androidx.test.espresso:espresso-intents:$VERSION"
            }
        }
    }

    class Google {
        public static final String auth = "com.google.android.gms:play-services-auth:17.0.0"

        public static final String fitness = "com.google.android.gms:play-services-fitness:18.0.0"

        public static final String materialDesign = "com.google.android.material:material:1.1.0-beta01"
    }

    class Kotlin {
        private static final String VERSION_STD = "1.3.50"
        private static final String VERSION_COROUTINES = "1.3.2"

        public static final String stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$VERSION_STD"

        public static final String coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$VERSION_COROUTINES"
        public static final String coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$VERSION_COROUTINES"
    }

    class Ext {
        public static final String fastExcel = "org.dhatim:fastexcel:0.10.9"

        public static final String mockito = "org.mockito:mockito-android:3.0.0"

        public static final String mpChart = "com.github.PhilJay:MPAndroidChart:v3.1.0"

        public static final String junit4 = "junit:junit:4.12"
    }

    /**
     * Project libraries, modules in the `libraries` folder
     * and are shared across several Feature modules
     */
    class Lib {
        public static final String core = ":libraries:core"
        public static final String data = ":libraries:data"
        public static final String ui = ":libraries:ui"
    }

    /**
     * Project features, modules in the `features` folder
     * and have their own UI
     */
    class Feature {
        public static final String export = ":features:export"
        public static final String glucose = ":features:glucose"
        public static final String googleFit = ":features:googlefit"
        public static final String insulin = ":features:insulin"
        public static final String overview = ":features:overview"
        public static final String settings = ":features:settings"
    }
}