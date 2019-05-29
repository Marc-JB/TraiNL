package nl.marc_apps.ovgo.dependency_injection

import dagger.Module
import dagger.Provides
import nl.marc_apps.ovgo.domainservices.PublicTransportDataRepository
import nl.marc_apps.ovgo.api.OVgoApiRepository
import javax.inject.Singleton

@Module
object OVgoModule {
    @Singleton
    @JvmStatic
    @Provides
    fun providePublicTransportDataRepository(): PublicTransportDataRepository {
        return OVgoApiRepository()
    }
}