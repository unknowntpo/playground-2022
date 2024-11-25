interface A {
	field1: string;
	// fileA, A.field2 is number
	field2: string;
}

interface Embeded {
	field1: string;
	field2: string[];
	// fileA doesn't have this
	field3: string;
	field5: {
		_x: string;
		_y: number;
	}
}

// fileA does not have this interface
interface B {
	field1: string;
}

type K = string;
