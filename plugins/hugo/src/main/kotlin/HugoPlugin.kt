package org.jetbrains.dokka.hugo

import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.base.renderers.PackageListCreator
import org.jetbrains.dokka.base.renderers.RootCreator
import org.jetbrains.dokka.base.resolvers.shared.RecognizedLinkFormat
import org.jetbrains.dokka.gfm.CommonmarkRenderer
import org.jetbrains.dokka.gfm.GfmPlugin
import org.jetbrains.dokka.pages.*
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.plugability.plugin
import org.jetbrains.dokka.plugability.query
import org.jetbrains.dokka.transformers.pages.PageTransformer
import java.lang.StringBuilder


class HugoPlugin : DokkaPlugin() {

    val hugoPreprocessors by extensionPoint<PageTransformer>()

    val renderer by extending {
        (CoreExtensions.renderer
                providing { HugoRenderer(it) }
                override plugin<GfmPlugin>().renderer)
    }

    val rootCreator by extending {
        hugoPreprocessors with RootCreator
    }

    val packageListCreator by extending {
        hugoPreprocessors providing {
            PackageListCreator(it, RecognizedLinkFormat.DokkaJekyll)
        } order { after(rootCreator) }
    }
}

class HugoRenderer(
    context: DokkaContext
) : CommonmarkRenderer(context) {

    override val preprocessors = context.plugin<HugoPlugin>().query { hugoPreprocessors }

    override fun buildPage(
        page: ContentPage,
        content: (StringBuilder, ContentPage) -> Unit
    ): String {
        val builder = StringBuilder()
        builder.append("+++\n")
        appendFrontMatter(page, builder)
        builder.append("+++\n")
        content(builder, page)
        return builder.toString()
    }

    private fun appendFrontMatter(page: ContentPage, builder: StringBuilder) {
        builder.append("title = \"${page.name}\"")
        builder.append("draft = false")
        builder.append("toc = false")
        builder.append("type = \"reference\"")

        // Add menu item for each package
        if (isPackage(page)) {
            builder.append("linktitle = \"${page.name}\"")
            builder.append("[menu.docs]")
            builder.append("  parent = \"hw-security-reference\"")
            builder.append("  weight = 1")
        }
    }

    private fun isPackage(page: ContentPage): Boolean {
        if (page.content.dci.kind == ContentKind.Packages) {
            return true
        }
        return false
    }
}
