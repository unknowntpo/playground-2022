local http = require("socket.http")
local response, error_message = http.request("http://example.com")

if not response then
  print("Error:", error_message)
else
  print(response)
end
