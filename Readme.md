<h1 align="center">
<a href="https://bvlj.github.io/projects/diab.html">
  <img src="fastlane/metadata/android/en-US/images/featureGraphic.png"/>
</a>
</h1>

<p align="center">
<a href="https://gitlab.com/bvlj/diab/pipelines"><img src="https://gitlab.com/bvlj/diab/badges/staging/build.svg" alt="Gitlab Pipeline"></a>
<a href="https://circleci.com/gh/bvlj/diab/tree/master"><img src="https://circleci.com/gh/bvlj/diab/tree/master.svg?style=svg" alt="CircleCI"></a>
<a href="https://codeclimate.com/github/bvlj/diab/maintainability"><img src="https://api.codeclimate.com/v1/badges/017008168f8e5910c7c2/maintainability" alt="Maintainability"></a>
<a href="https://ktlint.github.io/"><img src="https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg" alt="ktlint"></a>
</p>

<p align="center">
<b>Diab</b> is a smart opensource application that helps you managing your diabetes by
keeping track of your glucose values and insulin injections.
</p>

[<img src="https://f-droid.org/badge/get-it-on.png" alt="Get it on F-Droid" height="80">](https://f-droid.org/app/it.diab)

Using the data registered inside the app it's possible to generate a
customized plugin that once applied to the app will provide smart insights
for insulin dosages based on real-time context.

It's also possible to integrate the app with other fitness services to share
the data.

## Features

* Save records of glucose and insulin dosages
* Insulin suggestions plugin
* Export records as Excel file
* Reminder for checking again in case of hypoglycemia
* [optional] Google Fit integration

For upcoming features, see the [bug tracker](https://github.com/bvlj/diab/issues).

## Open source

### License

Released under the [GNU GPLv3](https://www.gnu.org/licenses/gpl-3.0.txt).

### Working with the repository

Clone the repository with git

```shell
git clone https://github.com/bvlj/diab
```

Setup the work environment

* Install git pre-push hooks (all the tests must pass before pushing to master)
* Install [ktlint](https://ktlint.github.io) (enforces code style guidelines)
* Prevent git from tracking changes to your trained plugin models

```shell
./_scripts/setup.sh
```

### Compile

The builds can be compiled using [gradle](https://gradle.org/).
The following build variants are available:

1. `oss`
2.  `googleFit`

#### oss builds

Builds the app without any fitness services integration.
The output is composed of 100% open source code.

```shell
./gradlew assembleOssRelease
```

#### googleFit builds

Builds the app with (optional) Google Fit integration.
The output includes proprietary libraries from Google used for Fit.

```shell
./gradlew assembleGoogleFitRelease
```

## Insulin suggestions plugin

> :warning: This feature could be dangerous, use with extreme caution. :warning:
>
> **DO NOT** blindly rely on it as while it's efficient enough for
> providing hints, it's NOT supposed to replace medical advices
> or suggestions from more-experienced humans.
>
> The developer and contributors disclaim any responsibility over any
> form of injury derived from (mis)usage of this feature.

The insulin suggestion plugin allows the app to provide smart insulin
dosage hints.

The plugin is created by using a machine-learning model created with data from
the app which is tested against a wide set of possible scenarios.

Given the strictly sensitive and personal nature of dealt with data, no plugin
is bundled with the app, nor made available for download.

### Build your own suggestion plugin

To create your customized insulin suggestions plugins using ml, see the
[readme found in the `ml` module](_ml/Readme.md).

It's also possible to build a plugin manually, but no documentation is available for the time being.
