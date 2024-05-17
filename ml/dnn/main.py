from deep_feedforward_neural_network_model import DNN

import numpy as np
from sklearn import datasets
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler

# Your DNN class code here

# Load and preprocess the dataset
iris = datasets.load_iris()
X = iris.data
Y = iris.target

# Convert to binary classification (only two classes)
binary_mask = Y < 2
X = X[binary_mask]
Y = Y[binary_mask]

# Split the data into training and test sets
X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.2, random_state=42)

# Standardize the data
scaler = StandardScaler()
X_train = scaler.fit_transform(X_train).T  # Transpose to match the shape (features, samples)
X_test = scaler.transform(X_test).T  # Transpose to match the shape (features, samples)
Y_train = Y_train.reshape(1, -1)  # Shape (1, samples)
Y_test = Y_test.reshape(1, -1)  # Shape (1, samples)

# Initialize and train the neural network
dnn = DNN(binary_classification=True)
layer_dims = [5, 5]  # Example layer dimensions
# FIXME: increase epoch to tune accuracy
dnn.train(X_train, Y_train, layer_dims, epoch=2, learning_rate=0.01, minibatch_size=16)

# Predict and evaluate on the test set
predictions = dnn.predict(X_test)
predictions = predictions > 0.5  # Convert probabilities to binary output

# Calculate accuracy
accuracy = np.mean(predictions == Y_test)
print(f"Test set accuracy: {accuracy * 100:.2f}%")
