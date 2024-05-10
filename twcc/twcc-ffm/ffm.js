async function main() {
  const body =
  {
    "model": "ffm-llama3-70b-chat",
    "messages": [
      {
        "role": "human",
        "content": "人口最多的國家是?"
      },
      {
        "role": "assistant",
        "content": "人口最多的國家是印度。"
      },
      {
        "role": "human",
        "content": "主要宗教為?"
      }],
    "parameters": {
      "max_new_tokens": 350,
      "temperature": 0.5,
      "top_k": 50,
      "top_p": 1,
      "frequence_penalty": 1
    }
  }
  const requestInit = {
    method: 'POST',
    // https://docs.twcc.ai/docs/user-guides/twcc/afs/afs-modelspace/available-model
    headers: {
      'X-API-KEY': process.env.TWCC_API_KEY,
      'X-API-HOST': 'afs-inference',
      'content-type': 'application/json'
    },
    body: JSON.stringify(body),
  };

  const apiUrl = 'https://api-ams.twcc.ai/api/models/conversation';
  const resp = await fetch(apiUrl, requestInit);
  console.log(await resp.text());
}

main()
