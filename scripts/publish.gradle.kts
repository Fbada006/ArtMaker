apply(plugin = "com.vanniktech.maven.publish")

rootProject.extra.apply {
    val libVersion = "1.1.0"
    set("libVersion", libVersion)
}