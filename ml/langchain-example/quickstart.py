from langchain_community.llms import Ollama

llm = Ollama(model="llama2")
# print(llm.invoke("how can langsmith help with testing?"))
print(
    llm.invoke(
        "show me example json object with 2 key, without any explaination, just give me object"
    )
)
