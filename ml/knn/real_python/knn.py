import matplotlib.pyplot as plt
import pandas as pd


def plot_rings_hist(abalone):
    # https://realpython.com/knn-python/#descriptive-statistics-from-the-abalone-dataset
    abalone["Rings"].hist(bins=15)
    plt.show()


def build_corr_matrix_for_rings(abalone):
    corrMatrix = abalone.corr()
    print(corrMatrix["Rings"])


def prepare_dataset():
    # data source from ics.uci.edu
    url = (
        "https://archive.ics.uci.edu/ml/machine-learning-databases"
        "/abalone/abalone.data"
    )

    # abalone = pd.read_csv(url, header=None)

    # We cache the data here
    abalone = pd.read_csv("./data/abalone.data", header=None)

    # Show head of dataset
    print("head\n", abalone.head())

    # Add Column of dataset
    abalone.columns = [
        "Sex",
        "Length",
        "Diameter",
        "Height",
        "Whole weight",
        "Shucked weight",
        "Viscera weight",
        "Shell weight",
        "Rings",
    ]

    return abalone


def main():
    abalone = prepare_dataset()
    # plot_rings_hist(abalone=abalone)
    build_corr_matrix_for_rings(abalone=abalone)


# Using the special variable
# __name__
if __name__ == "__main__":
    main()
