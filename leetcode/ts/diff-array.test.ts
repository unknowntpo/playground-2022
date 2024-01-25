import { describe, expect, test, it } from '@jest/globals';
import { getPriority } from 'os';


class Arr {
  private arr:Array<number>;
  private diff:Array<number>;

  constructor(arr: Array<number>) {
    if ( arr.length == 0 ){
      throw new Error(`length of array should be greater than 0, got ${arr.length}`);
    }
    this.arr = arr;
    this.diff[0] = this.arr[0];
    for (let i = 1; i < arr.length;i++) {
      this.diff[i] = this.arr[i] - this.arr[i-1];
    }
  }

  // do operation of [i, j)
  add(i:number , j:number, num: number) {
    this.diff[i] += num;
  }
  sub(i:number , j:number) {

  }

  getOrigArrayFromDiffArray(): Array<number> {
    for ()

  }
}
const addToDiffArray = (diffArr: [])


describe('diff array', ()=>{
  test('add 3 to element in intervals',()=>{
    const arr = [1,3,5,6,7, 9]; 
    let diffArray = new Arr(arr);
    const start  = 1;
    const end = 3;
    diffArray.add(start,end, 3);
    const res = diffArray.getOrigArrayFromDiffArray(); 
    for (let i = 0; i < start; i ++) {
      expect(res[i]).toBe(arr[i]);
    }
    for (let i = start;i < end; i ++) {
      expect(res[i]).toBe(arr[i] + 3);
    }
    for (let i = end; i < arr.length; i ++) {
      expect(res[i]).toBe(arr[i]);
    }
  })
})