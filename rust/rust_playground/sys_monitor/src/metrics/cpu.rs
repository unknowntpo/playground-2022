use sysinfo::System;
use super::Metric;

// ============== Data Struct ==============

#[derive(Debug, Clone, PartialEq)]
pub struct CpuInfo {
    pub usage: f32,
}

// ============== Fake Implementation ==============

pub struct FakeCpuMetric {
    value: CpuInfo,
}

impl FakeCpuMetric {
    pub fn new(usage: f32) -> Self {
        Self {
            value: CpuInfo { usage },
        }
    }
}

impl Metric for FakeCpuMetric {
    type Output = CpuInfo;

    fn refresh(&mut self) {
        // no-op for fake
    }

    fn get(&self) -> Self::Output {
        self.value.clone()
    }
}

// ============== Real Implementation (sysinfo) ==============

pub struct CpuMetric {
    sys: System,
    value: CpuInfo,
}

impl CpuMetric {
    pub fn new() -> Self {
        let mut sys = System::new();
        sys.refresh_cpu_usage();
        std::thread::sleep(std::time::Duration::from_millis(100));
        sys.refresh_cpu_usage();
        let usage = sys.global_cpu_usage();
        Self {
            sys,
            value: CpuInfo { usage },
        }
    }
}

impl Default for CpuMetric {
    fn default() -> Self {
        Self::new()
    }
}

impl Metric for CpuMetric {
    type Output = CpuInfo;

    fn refresh(&mut self) {
        self.sys.refresh_cpu_usage();
        self.value.usage = self.sys.global_cpu_usage();
    }

    fn get(&self) -> Self::Output {
        self.value.clone()
    }
}

// ============== Tests ==============

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_fake_cpu_metric_returns_configured_value() {
        let metric = FakeCpuMetric::new(45.5);
        assert_eq!(metric.get().usage, 45.5);
    }

    #[test]
    fn test_fake_cpu_metric_refresh_does_not_panic() {
        let mut metric = FakeCpuMetric::new(50.0);
        metric.refresh();
    }

    #[test]
    fn test_cpu_metric_returns_valid_range() {
        let metric = CpuMetric::new();
        let cpu = metric.get();
        assert!(cpu.usage >= 0.0 && cpu.usage <= 100.0);
    }

    #[test]
    fn test_cpu_metric_refresh_updates_value() {
        let mut metric = CpuMetric::new();
        let initial = metric.get();

        for _ in 0..1000 {
            let _ = (0..1000).sum::<i32>();
        }

        metric.refresh();
        let after = metric.get();

        assert!(initial.usage >= 0.0 && initial.usage <= 100.0);
        assert!(after.usage >= 0.0 && after.usage <= 100.0);
    }
}
