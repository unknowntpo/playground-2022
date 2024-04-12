# https://www.youtube.com/watch?v=_j7JEDWuqLE&t=175s

from dotenv import find_dotenv, load_dotenv
from transformers import pipeline

from langchain_core.prompts import PromptTemplate
from langchain.chains import LLMChain
from langchain_community.llms import OpenAI


load_dotenv(find_dotenv())

# image-to-text
def img2text(url):
  # Use a pipeline as a high-level helper
  image_to_text = pipeline("image-to-text", model="Salesforce/blip-image-captioning-base")

  text = image_to_text(url)[0]["generated_text"]

  print(text)
  return text

# llm
def generate_story(scenario):
    template = """
    You are a story teller:
    You can generate a short story based on a simple narrative, the story should be no more than 20 words;

    CONTEXT: {scenario}
    STORY:
    """

    prompt = PromptTemplate(template=template, input_variables=["scenario"])
    story_llm = LLMChain(llm=OpenAI(
        model_name="gpt-3.5-turbo", temperature=1
    ), prompt=prompt, verbose=True)

    story = story_llm.predict(scenario=scenario)

    return story
# text to speech

scenario = img2text("cat.jpeg")
story = generate_story(scenario)
print(story)

