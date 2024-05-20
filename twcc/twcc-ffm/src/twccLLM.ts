import {
	ChatResponse,
	ChatResponseChunk,
	CompletionResponse,
	LLM,
	LLMChatParamsNonStreaming,
	LLMChatParamsStreaming,
	LLMCompletionParamsNonStreaming,
	LLMCompletionParamsStreaming,
	LLMMetadata,
	Tokenizers
} from 'llamaindex';

function sum(x: number, y: number): number { return x + y };


class TwccLLM implements LLM {
	metadata: LLMMetadata;
	constructor() {
		this.metadata = {
			model: 'abc',
			temperature: 0,
			topP: 1,
			maxTokens: 350,
			contextWindow: 1000,
			tokenizer: undefined,
		}
	}

	chat(params: LLMChatParamsStreaming<object, object>): Promise<AsyncIterable<ChatResponseChunk<object>>>;
	chat(params: LLMChatParamsNonStreaming<object, object>): Promise<ChatResponse<object>>;
	chat(params: unknown): Promise<AsyncIterable<ChatResponseChunk<object>>> | Promise<ChatResponse<object>> {
		throw new Error('Method not implemented.');
	}
	complete(params: LLMCompletionParamsStreaming): Promise<AsyncIterable<CompletionResponse>>;
	complete(params: LLMCompletionParamsNonStreaming): Promise<CompletionResponse>;
	complete(params: unknown): Promise<AsyncIterable<CompletionResponse>> | Promise<CompletionResponse> {
		throw new Error('Method not implemented.');
	}
}


export { sum, TwccLLM }
