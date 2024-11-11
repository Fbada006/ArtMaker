apply(plugin = "com.vanniktech.maven.publish")

rootProject.extra.apply {
    val libVersion = "3.0.0"
    set("libVersion", libVersion)
}