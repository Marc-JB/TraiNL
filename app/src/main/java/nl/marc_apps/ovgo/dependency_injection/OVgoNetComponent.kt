package nl.marc_apps.ovgo.dependency_injection

object OVgoNetComponent {
    val netComponent = DaggerOVgoComponent.builder().oVgoModule(OVgoModule).build()
}