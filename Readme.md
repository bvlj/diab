[![Build Status](https://api.travis-ci.org/bvlj/diab.svg)](https://travis-ci.org/bvlj/diab)
[![CircleCI](https://circleci.com/gh/bvlj/diab/tree/staging.svg?style=svg)](https://circleci.com/gh/bvlj/diab/tree/staging)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/5ba8b95a14d04075b86cce7ce71c46f0)](https://www.codacy.com/app/bvlj/diab?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=bvlj/diab&amp;utm_campaign=Badge_Grade)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)

Diab
=====

![hero](docs/assets/hero.png)

**Diab** is a smart opensource application that helps you managing your diabetes by
keeping track of your glucose values and insulin injections.

Using the data registered inside the app it's possible to generate a
customized plugin that once applied to the app will provide smart insights
for insulin dosages based on real-time context.

It's also possible to integrate the app with other fitness services to share
the data.

[<img src="https://f-droid.org/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80">](https://f-droid.org/packages/it.diab/)

## Features

* Save records of glucose and insulin dosages
* Insulin suggestions plugin
* Export records as Excel file
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
./setup.sh
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
[readme found in the `ml` module](ml/Readme.md).

It's also possible to build a plugin manually, but no documentation is available for the time being.
