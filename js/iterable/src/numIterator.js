class Range {
	/**
	 * @param {number} from
	 * @param {number} to
	 */
	constructor(from, to) {
		this.from = from;
		this.to = to;
	}
	[Symbol.iterator]() {
		let current = this.from;
		const end = this.to;
		return {
			next() {
				if (current <= end) {
					return { value: current++, done: false };
				} else {
					return { value: undefined, done: true };
				}
			}
		};
	}
}

export { Range };

