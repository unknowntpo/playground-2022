use sysinfo::System;
use super::Metric;

// ============== Data Struct ==============

#[derive(Debug, Clone, PartialEq)]
pub struct CoreInfo {
    pub name: String,
    pub usage: f32,
}

#[derive(Debug, Clone, PartialEq)]
pub struct CpuInfo {
    pub cores: Vec<CoreInfo>,
    pub avg_usage: f32,
}

impl CpuInfo {
    pub fn new(cores: Vec<CoreInfo>) -> Self {
        let avg_usage = if cores.is_empty() {
            0.0
        } else {
            cores.iter().map(|c| c.usage).sum::<f32>() / cores.len() as f32
        };
        Self { cores, avg_usage }
    }
}

// ============== Fake Implementation ==============

pub struct FakeCpuMetric {
    value: CpuInfo,
}

impl FakeCpuMetric {
    /// Create fake CPU with single average value (backwards compatible)
    pub fn new(usage: f32) -> Self {
        Self::with_cores(vec![usage; 4]) // Default 4 cores
    }

    /// Create fake CPU with per-core values
    pub fn with_cores(usages: Vec<f32>) -> Self {
        let cores: Vec<CoreInfo> = usages
            .into_iter()
            .enumerate()
            .map(|(i, usage)| CoreInfo {
                name: format!("Core {}", i),
                usage,
            })
            .collect();
        Self {
            value: CpuInfo::new(cores),
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

        let value = Self::collect_cpu_info(&sys);
        Self { sys, value }
    }

    fn collect_cpu_info(sys: &System) -> CpuInfo {
        let cores: Vec<CoreInfo> = sys
            .cpus()
            .iter()
            .enumerate()
            .map(|(i, cpu)| CoreInfo {
                name: format!("Core {}", i),
                usage: cpu.cpu_usage(),
            })
            .collect();
        CpuInfo::new(cores)
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
        self.value = Self::collect_cpu_info(&self.sys);
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
        assert_eq!(metric.get().avg_usage, 45.5);
        assert_eq!(metric.get().cores.len(), 4);
    }

    #[test]
    fn test_fake_cpu_metric_with_cores() {
        let metric = FakeCpuMetric::with_cores(vec![10.0, 20.0, 30.0, 40.0]);
        let cpu = metric.get();
        assert_eq!(cpu.cores.len(), 4);
        assert_eq!(cpu.cores[0].usage, 10.0);
        assert_eq!(cpu.cores[3].usage, 40.0);
        assert_eq!(cpu.avg_usage, 25.0); // (10+20+30+40)/4
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
        assert!(cpu.avg_usage >= 0.0 && cpu.avg_usage <= 100.0);
        assert!(!cpu.cores.is_empty());
        for core in &cpu.cores {
            assert!(core.usage >= 0.0 && core.usage <= 100.0);
        }
    }

    #[test]
    fn test_cpu_metric_refresh_updates_value() {
        let mut metric = CpuMetric::new();
        metric.refresh();
        let cpu = metric.get();
        assert!(cpu.avg_usage >= 0.0 && cpu.avg_usage <= 100.0);
    }
}
