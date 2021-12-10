package nl.marc_apps.ovgo.data.api

import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress

object CloudflareDns {
    const val HOST_NAME = "cloudflare-dns.com"

    const val RESOLVER_URL = "https://${HOST_NAME}/dns-query"

    inline val alternativeHosts: Array<InetAddress>
        get() = arrayOf(
            Inet4Address.getByName("1.1.1.1"),
            Inet4Address.getByName("1.0.0.1"),
            Inet6Address.getByName("2606:4700:4700::1111"),
            Inet6Address.getByName("2606:4700:4700::1001")
        )
}
