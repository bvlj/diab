/*
 * Copyright (c) 2020 Bevilacqua Joey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.diab.build

@SuppressWarnings("SpellCheckingInspection")
class Deps {

    // AndroidX - https://developer.android.com/jetpack/androidx/versions
    class AndroidX {
        // AppCompat - https://developer.android.com/jetpack/androidx/releases/appcompat
        public static final String appCompat = "androidx.appcompat:appcompat:1.1.0"

        // Constraint Layout - https://developer.android.com/jetpack/androidx/releases/constraintlayout
        public static final String constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta4"

        // Dynamic Animation - https://developer.android.com/jetpack/androidx/releases/dynamicanimation
        public static final String dynamicAnim = "androidx.dynamicanimation:dynamicanimation:1.0.0"

        // Fragment - https://developer.android.com/jetpack/androidx/releases/fragment
        public static final String fragment = "androidx.fragment:fragment-ktx:1.2.4"

        // Core - KTX - https://developer.android.com/jetpack/androidx/releases/core
        public static final String ktx = "androidx.core:core-ktx:1.3.0"

        // Material Design - https://material.io/develop - https://github.com/material-components/material-components-android/releases
        public static final String material = "com.google.android.material:material:1.1.0"

        // Paging - https://developer.android.com/jetpack/androidx/releases/paging
        public static final String paging = "androidx.paging:paging-runtime-ktx:2.1.2"

        // Preference - https://developer.android.com/jetpack/androidx/releases/preference
        public static final String preference = "androidx.preference:preference:1.1.1"

        // RecyclerView - https://developer.android.com/jetpack/androidx/releases/recyclerview
        public static final String recyclerView = "androidx.recyclerview:recyclerview:1.1.0"

        // WorkManager - https://developer.android.com/jetpack/androidx/releases/work
        public static final String workManager = "androidx.work:work-runtime-ktx:2.3.4"

        // Lifecycle - https://developer.android.com/jetpack/androidx/releases/lifecycle
        class Lifecycle {
            private static final String VERSION = "2.2.0"

            public static final String compiler = "androidx.lifecycle:lifecycle-compiler:$VERSION"
            public static final String extensions = "androidx.lifecycle:lifecycle-extensions:$VERSION"
            public static final String liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$VERSION"
            public static final String runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$VERSION"
            public static final String viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$VERSION"
        }

        // Room - https://developer.android.com/jetpack/androidx/releases/room
        class Room {
            private static final String VERSION = "2.2.5"

            public static final String compiler = "androidx.room:room-compiler:$VERSION"
            public static final String ktx = "androidx.room:room-ktx:$VERSION"
            public static final String runtime = "androidx.room:room-runtime:$VERSION"
        }
    }

    class FastExcel {
        private static final String VERSION = "0.10.12"

        public static final String reader = "org.dhatim:fastexcel-reader:$VERSION"
        public static final String writer = "org.dhatim:fastexcel:$VERSION"
    }

    // Kotlin - https://github.com/JetBrains/kotlin/releases
    class Kotlin {
        private static final String VERSION = "1.3.72"

        class StdLib {

            public static final String common = "org.jetbrains.kotlin:kotlin-stdlib-common:$VERSION"
            public static final String jvm = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$VERSION"
        }

        // Coroutines - https://github.com/Kotlin/kotlinx.coroutines/releases
        class Coroutines {
            private static final String VERSION = "1.3.4"

            public static final String common = "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$VERSION"
            public static final String jvm = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$VERSION"
            public static final String android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$VERSION"
        }

        class Test {
            public static final String common = "org.jetbrains.kotlin:kotlin-test-common:$VERSION"
            public static final String commonAnnotations = "org.jetbrains.kotlin:kotlin-test-annotations-common:$VERSION"
            public static final String junit = "org.jetbrains.kotlin:kotlin-test-junit:$VERSION"
        }
    }

    class Square {
        // LeakCanary - https://github.com/square/leakcanary/releases
        class LeakCanary {
            private static final String VERSION = "2.2"

            public static final String android = "com.squareup.leakcanary:leakcanary-android:$VERSION"
        }
    }

    /**
     * Project libraries, modules in the `libraries` folder
     * and are shared across several Features modules
     */
    class Libraries {
        public static final String core = ":libraries:core"
        public static final String roboto = ":libraries:roboto"
        public static final String ui = ":libraries:ui"
    }

    /**
     * Project features, modules in the `features` folder
     */
    class Features {
        public static final String editor = ":features:editor"
        public static final String insulin = ":features:insulin"
        public static final String overview = ":features:overview"
        public static final String preferences = ":features:preferences"
    }
}
