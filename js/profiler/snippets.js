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

async function main() {



  // [START profiler_setup_nodejs_compute_engine]
  await require('@google-cloud/profiler').start({
    projectId: 'web-service-design',
    serviceContext: {
      service: 'web-service-design-cloud-profiler-sample',
      version: '1.0.0',
    },
  });

  function computeFibonacci(n) {
    if (n <= 1) return n;
    return computeFibonacci(n - 1) + computeFibonacci(n - 2);
  }
  console.log(computeFibonacci(40));

  // [END profiler_setup_nodejs_compute_engine]
}

main()
