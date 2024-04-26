const fs = require('fs');

describe('Detech open handle', () => {
  let fileContent = '';
  it('should read from file', () => {
    const stream = fs.createReadStream('file.txt')
    stream.on('data', (chunk) => {
      fileContent += chunk.toString();
    });
    stream.on('end', () => {
      // Assert the file content
      expect(fileContent).toBe('hello world\n');

      // Close the stream when it's no longer needed
      stream.close();
    });
  });
})
