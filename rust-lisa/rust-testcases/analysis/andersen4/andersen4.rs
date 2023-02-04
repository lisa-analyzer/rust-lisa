fn main() {
    let z = 1;
    let y = &z;
    let x = &y;
    let w = &x;
    let a = w;
    let b = *w;
    let c = **a;
}
