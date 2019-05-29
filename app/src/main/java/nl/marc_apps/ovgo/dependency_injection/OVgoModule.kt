package nl.marc_apps.ovgo.dependency_injection

import dagger.Module
import dagger.Provides
import nl.marc_apps.ovgo.domainservices.PublicTransportDataRepository
import nl.marc_apps.ovgo.repositories.OVgoApiRepository
import javax.inject.Singleton

@Module
object OVgoModule {
    @JvmStatic
    @Provides
    fun providePublicTransportDataRepository(): PublicTransportDataRepository {
        return OVgoApiRepository()
    }
}