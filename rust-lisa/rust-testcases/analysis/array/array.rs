fn main(y : i32) -> i32 {
    let x : [i32; 4] = [1, 2, 4, 5];
    
    if true {
    	x[0] = -2;
    } else {
    	x[0] = 7;
    }
    
    let i : i32 = 0; 
    while i < 100 {
    	x[0] += 1;
    	i += 1;
    }
    
    return 5;
}