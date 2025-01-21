use fuser::Filesystem;

#[derive(Debug)]
struct DummyFS {}

impl Filesystem for DummyFS {}
