interface A {
	field1: string;
	field2: number;
}

interface Embeded {
	field1: string;
	field2: string[];
	// fileB doesn't have this
	field4: string;
	field5: {
		_x: string;
		// diff from fileB
		_y: string;
	}

}
