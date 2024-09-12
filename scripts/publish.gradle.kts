apply(plugin = "com.vanniktech.maven.publish")

rootProject.extra.apply {
    val libVersion = "2.2.0"
    set("libVersion", libVersion)
}