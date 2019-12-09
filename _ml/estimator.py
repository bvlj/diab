from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import pandas as pd
import tensorflow as tf
import zipfile

ALLOWED_TIME_FRAMES = [1, 3, 5]
BATCH_SIZE = 100
COLUMN_NAMES = ["value", "eatLevel", "insulin"]
PREDICT_NAMES = ["value", "eatLevel"]


def load_data(y_name="insulin", time_frame=1):
    train_path = "data/train_{}.csv".format(time_frame)
    test_path = "data/test_{}.csv".format(time_frame)
    predict_path = "data/prediction.csv"

    train = pd.read_csv(train_path,
                        names=COLUMN_NAMES,
                        skipinitialspace=True,
                        skip_blank_lines=True,
                        skiprows=1)
    train_x, train_y = train, train.pop(y_name)

    test = pd.read_csv(test_path,
                       names=COLUMN_NAMES,
                       skipinitialspace=True,
                       skip_blank_lines=True,
                       skiprows=1)
    test_x, test_y = test, test.pop(y_name)

    prediction = pd.read_csv(predict_path,
                             names=PREDICT_NAMES,
                             skipinitialspace=True,
                             skip_blank_lines=True,
                             skiprows=1)

    return (train_x, train_y), (test_x, test_y), prediction


def train_input_fn(features, labels, batch_size):
    data_set = tf.data.Dataset.from_tensor_slices((dict(features), labels))
    data_set = data_set.shuffle(1000).repeat().batch(batch_size)

    return data_set


def eval_input_fn(features, labels, batch_size):
    features = dict(features)
    if labels is None:
        inputs = features
    else:
        inputs = (features, labels)

    data_set = tf.data.Dataset.from_tensor_slices(inputs)

    assert batch_size is not None, "batch_size must not be None"
    data_set = data_set.batch(batch_size)

    return data_set


def get_output_data(results):
    input_list = []

    with open("data/prediction.csv", "r") as input_file:
        # Skip the header
        iterator = iter(input_file)
        next(iterator)

        for line in iterator:
            input_list.append(line.split(",")[0].rstrip())
        input_file.close()

    assert len(input_list) == len(results), \
        "The lists must have the same length! [{}, {}]".format(len(input_list), len(results))
    return input_list, results


def run(time_frame):
    (train_x, train_y), (test_x, test_y), predict_x = load_data(time_frame=time_frame)
    feature_columns = []
    for key in train_x.keys():
        feature_columns.append(tf.feature_column.numeric_column(key=key))

    classifier = tf.estimator.DNNRegressor(feature_columns=feature_columns,
                                           hidden_units=[10, 10],
                                           model_dir="out/{}".format(time_frame))

    classifier.train(input_fn=lambda: train_input_fn(train_x, train_y, BATCH_SIZE),
                     steps=1000)

    eval_result = classifier.evaluate(input_fn=lambda: eval_input_fn(test_x, test_y, BATCH_SIZE))
    print("\nLoss: {loss:0.3f}\n".format(**eval_result))

    predictions = classifier.predict(input_fn=lambda: eval_input_fn(predict_x, None, BATCH_SIZE))

    estimated = []
    for item in predictions:
        print(item["predictions"][0])
        estimated.append(item["predictions"][0])

    with open("export/estimator_{}.json".format(time_frame), "w+") as output:
        input_list, result_list = get_output_data(estimated)

        output.write("{")

        size = len(input_list)
        i = 0
        while i < size:
            output.write("\n  \"{}\": {}".format(input_list[i], result_list[i]))
            i = i + 1
            if i != size:
                output.write(",")

        output.write("\n}")
        output.close()


def export():
    zip_file = zipfile.ZipFile("export/plugin.zip", mode='w')
    try:
        for time_frame in ALLOWED_TIME_FRAMES:
            zip_file.write(filename="export/estimator_{}.json".format(time_frame),
                           arcname="estimator_{}.json".format(time_frame))
    finally:
        zip_file.close()
    print("Done! Plugin available at \"export/plugin.zip\"")


def main(args):
    for time_frame in ALLOWED_TIME_FRAMES:
        run(time_frame)
    export()


if __name__ == "__main__":
    # tf.logging.set_verbosity(tf.logging.INFO)
    tf.app.run(main)
