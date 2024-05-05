import logging
import sys

logging.basicConfig(stream=sys.stdout, level=logging.INFO)
logging.getLogger().addHandler(logging.StreamHandler(stream=sys.stdout))


from llama_index.core import SummaryIndex, Settings
from llama_index.readers.mongodb import SimpleMongoReader
from llama_index.llms.ollama import Ollama

# from IPython.display import Markdown, display
import os


# nomic embedding model
# Settings.embed_model = OllamaEmbedding(model_name="nomic-embed-text")

# ollama
Settings.llm = Ollama(model="llama3", request_timeout=360.0)


host = "localhost"
port = 27017
db_name = "test"
collection_name = "llamaindex"
# query_dict is passed into db.collection.find()
query_dict = {}
field_names = ["text"]
reader = SimpleMongoReader(host, port)
documents = reader.load_data(
    db_name, collection_name, field_names, query_dict=query_dict
)


index = SummaryIndex.from_documents(documents)

# set Logging to DEBUG for more detailed outputs
query_engine = index.as_query_engine()
response = query_engine.query("What did i eat at 2024/5/8? ")
print(response)
