# ML estimator model for insulin suggestions plugin

## Setup

Required software:

* python 3.6
* pip3

Install dependencies

```shell
pip3 install -r requirements.txt
```

## Prepare the data for training

### Export data from the app

From the app settings, click on _"Export data"_ and authenticate to allow
the app to save your glucose data for ml training.

#### Import using adb

Enable usb debugging on your device, plug it in and then run on your pc
```shell
bash import.sh data
```

#### Import manually

Copy the files from the `Documents/diab` directory inside the device memory
to the 'data' directory.

#### Setup the tests

Fill the `data/test_*.csv` files are automatically filled with known-good
results, but it's recommended to check and eventually fine tune them.

## Train the model and get the results

The app uses some [json](https://json.org) files to look at references of
the model results instead of shipping the real model itself in
order to allow lower-end devices to use this feature too.

All you need to do is execute the python script:

`python3 estimator.py`

## Install the plugin

Once the training has been completed, the plugin
will be available at the following path from the repository
root: `ml/export/plugin.zip`.

Send this file to the device, open the app settings, click on
_"Suggestions plugin"_, select the zip file and wait until it's
installed.

To update the plugin, repeat this procedure.

## Disable tracking git changes of sensitive files

Sensitive file changes tracking should be disabled by running the
`setup.sh` file found in the root of this repository, or
by updating the git index manually:

```shell
 git update-index --skip-worktree _ml/data/*
 git update-index --skip-worktree _ml/export/*
```
