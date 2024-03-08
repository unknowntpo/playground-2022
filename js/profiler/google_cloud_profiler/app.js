// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

'use strict';



const express = require('express');
const app = express();

// Your existing Fibonacci function (you'll need to provide this)
function fib(n) {
  // ... your Fibonacci calculation logic ...
  if (n <= 1) return n;
  return fib(n - 1) + fib(n - 2);
}

async function main() {
  // ... your existing profiler code ...
  // [START profiler_setup_nodejs_compute_engine]
  await require('@google-cloud/profiler').start({
    projectId: 'web-service-design',
    serviceContext: {
      service: 'web-service-design-cloud-profiler-sample',
      version: '1.1.0',
    },
  });

  // Define a route to handle the Fibonacci request
  app.get('/fibonacci/:n', (req, res) => {
    const n = parseInt(req.params.n);
    console.log(`n = ${n}`);
    const result = fib(n);
    res.json({ result }); 
  });

  // Start the Express server
  const port = 9090; // You can change the port if needed
  app.listen(port, () => {
    console.log(`Server listening on port ${port}`);
  });
}

main(); 
