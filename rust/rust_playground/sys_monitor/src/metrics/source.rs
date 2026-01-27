use super::{
    CpuInfo, CpuMetric, DiskInfo, DiskMetric, FakeCpuMetric, FakeDiskMetric, FakeMemMetric,
    MemInfo, MemMetric, Metric,
};

// ============== MetricsSource ==============

pub struct MetricsSource {
    cpu: Box<dyn Metric<Output = CpuInfo>>,
    mem: Box<dyn Metric<Output = MemInfo>>,
    disk: Box<dyn Metric<Output = Vec<DiskInfo>>>,

    // Cached values for borrowing
    cpu_val: CpuInfo,
    mem_val: MemInfo,
    disk_val: Vec<DiskInfo>,
}

impl MetricsSource {
    /// Create MetricsSource with custom metrics (for dependency injection)
    pub fn new(
        cpu: Box<dyn Metric<Output = CpuInfo>>,
        mem: Box<dyn Metric<Output = MemInfo>>,
        disk: Box<dyn Metric<Output = Vec<DiskInfo>>>,
    ) -> Self {
        let cpu_val = cpu.get();
        let mem_val = mem.get();
        let disk_val = disk.get();

        Self {
            cpu,
            mem,
            disk,
            cpu_val,
            mem_val,
            disk_val,
        }
    }

    /// Create MetricsSource with real system metrics
    pub fn with_real_metrics() -> Self {
        Self::new(
            Box::new(CpuMetric::new()),
            Box::new(MemMetric::new()),
            Box::new(DiskMetric::new()),
        )
    }

    /// Create MetricsSource with fake metrics (for testing)
    pub fn with_fake_metrics(cpu_usage: f32, mem_total: u64, mem_used: u64) -> Self {
        Self::new(
            Box::new(FakeCpuMetric::new(cpu_usage)),
            Box::new(FakeMemMetric::new(mem_total, mem_used)),
            Box::new(FakeDiskMetric::single("/", 500_000_000_000, 250_000_000_000)),
        )
    }

    /// Refresh all metrics
    pub fn refresh(&mut self) {
        self.cpu.refresh();
        self.mem.refresh();
        self.disk.refresh();

        // Update cached values
        self.cpu_val = self.cpu.get();
        self.mem_val = self.mem.get();
        self.disk_val = self.disk.get();
    }

    /// Get CPU info
    pub fn cpu(&self) -> &CpuInfo {
        &self.cpu_val
    }

    /// Get memory info
    pub fn mem(&self) -> &MemInfo {
        &self.mem_val
    }

    /// Get disk info
    pub fn disks(&self) -> &Vec<DiskInfo> {
        &self.disk_val
    }
}

// ============== Tests ==============

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_metrics_source_with_fake() {
        let source = MetricsSource::with_fake_metrics(50.0, 16_000_000_000, 8_000_000_000);

        assert_eq!(source.cpu().usage, 50.0);
        assert_eq!(source.mem().total, 16_000_000_000);
        assert_eq!(source.mem().used, 8_000_000_000);
        assert!(!source.disks().is_empty());
    }

    #[test]
    fn test_metrics_source_with_real() {
        let source = MetricsSource::with_real_metrics();

        // Should return valid values
        assert!(source.cpu().usage >= 0.0 && source.cpu().usage <= 100.0);
        assert!(source.mem().total > 0);
        assert!(source.mem().used <= source.mem().total);
    }

    #[test]
    fn test_metrics_source_refresh() {
        let mut source = MetricsSource::with_real_metrics();

        // Initial values
        let initial_cpu = source.cpu().usage;

        // Refresh
        source.refresh();

        // Should still be valid
        assert!(source.cpu().usage >= 0.0 && source.cpu().usage <= 100.0);

        // Values might change (or not), but should be valid
        let _ = initial_cpu; // acknowledge we captured it
    }
}
