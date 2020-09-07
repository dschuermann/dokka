# Dokka Hugo Plugin

Extends [Dokka](https://github.com/Kotlin/dokka) with a Hugo Plugin.

## Build

``./gradlew plugins:hugo:build``

## Requirements

* Currently uses the Hugo academic theme
* Requires special docs template called "reference"
* Requires shortcode to insert markdown in html. Put `md.html` in your "shortcodes" folder with the content:
```
{{ .Inner }}
```
