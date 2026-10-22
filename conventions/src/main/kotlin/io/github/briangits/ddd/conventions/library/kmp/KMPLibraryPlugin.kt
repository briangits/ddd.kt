package io.github.briangits.ddd.conventions.library.kmp

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KMPLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.apply("org.jetbrains.kotlin.multiplatform")
            plugins.apply("com.android.kotlin.multiplatform.library")

            extensions.configure<KotlinMultiplatformExtension> {
                jvm()

                js {
                    browser()
                    nodejs()
                }

                @OptIn(ExperimentalWasmDsl::class)
                wasmJs {
                    browser()
                    nodejs()
                }

                iosX64()
                iosArm64()
                iosSimulatorArm64()

                macosArm64()

                linuxX64()
                linuxArm64()

                mingwX64()

                targets.withType<KotlinMultiplatformAndroidLibraryTarget>().configureEach {
                    with(it) {
                        namespace = project.group.toString()
                        compileSdk = 37
                        minSdk = 21

                        compilerOptions {
                            jvmTarget.set(JvmTarget.JVM_17)
                        }
                    }
                }
            }
        }
    }
}