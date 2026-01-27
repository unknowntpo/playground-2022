mod cpu;
mod mem;
mod disk;

// Re-export types
pub use cpu::{CpuInfo, CpuMetric, FakeCpuMetric};
pub use mem::{MemInfo, MemMetric, FakeMemMetric};
pub use disk::{DiskInfo, DiskMetric, FakeDiskMetric};

// ============== Trait ==============

pub trait Metric {
    type Output;
    fn refresh(&mut self);
    fn get(&self) -> Self::Output;
}
