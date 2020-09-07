import org.jetbrains.registerDokkaArtifactPublication

dependencies {
    implementation(project(":plugins:base"))
    implementation(project(":plugins:gfm"))
}

registerDokkaArtifactPublication("hugoPlugin") {
    artifactId = "hugo-plugin"
}
