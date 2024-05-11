
async function chain(prompt) {
  //const prompt = `請幫我生成一個自相矛盾的兒童遊戲規則描述`

  const body =
  {
    "model": "ffm-llama3-70b-chat",
    "messages": [
      {
        "role": "human",
        "content": `${prompt}`
      },
    ],
    "parameters": {
      "max_new_tokens": 350,
      "temperature": 0.1,
      "top_k": 30,
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
  const text = await resp.text();
  const respObj = JSON.parse(text);

  console.log(respObj.generated_text);

  return respObj.generated_text;
}

async function main() {
  const step1Res = await chain('你是一個台灣著名的法學教授，請幫我生成一個人類可以一眼認出的，自相矛盾的法律規則描述，只需要給我描述，不需要給我其他的文字');
  chain('請檢測以下這段法律規則描述中，矛盾的部分' + step1Res)
}

main()

