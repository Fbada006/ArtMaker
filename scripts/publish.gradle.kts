apply(plugin = "com.vanniktech.maven.publish")

rootProject.extra.apply {
    val libVersion = "2.3.0"
    set("libVersion", libVersion)
}