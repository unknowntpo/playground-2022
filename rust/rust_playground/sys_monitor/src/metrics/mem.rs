use sysinfo::System;
use super::Metric;

// ============== Data Struct ==============

#[derive(Debug, Clone, PartialEq)]
pub struct MemInfo {
    pub total: u64,
    pub used: u64,
    pub percent: f32,
}

// ============== Fake Implementation ==============

pub struct FakeMemMetric {
    value: MemInfo,
}

impl FakeMemMetric {
    pub fn new(total: u64, used: u64) -> Self {
        let percent = (used as f32 / total as f32) * 100.0;
        Self {
            value: MemInfo { total, used, percent },
        }
    }
}

impl Metric for FakeMemMetric {
    type Output = MemInfo;

    fn refresh(&mut self) {
        // no-op for fake
    }

    fn get(&self) -> Self::Output {
        self.value.clone()
    }
}

// ============== Real Implementation (sysinfo) ==============

pub struct MemMetric {
    sys: System,
    value: MemInfo,
}

impl MemMetric {
    pub fn new() -> Self {
        let mut sys = System::new();
        sys.refresh_memory();
        let total = sys.total_memory();
        let used = sys.used_memory();
        let percent = (used as f32 / total as f32) * 100.0;
        Self {
            sys,
            value: MemInfo { total, used, percent },
        }
    }
}

impl Default for MemMetric {
    fn default() -> Self {
        Self::new()
    }
}

impl Metric for MemMetric {
    type Output = MemInfo;

    fn refresh(&mut self) {
        self.sys.refresh_memory();
        let total = self.sys.total_memory();
        let used = self.sys.used_memory();
        self.value = MemInfo {
            total,
            used,
            percent: (used as f32 / total as f32) * 100.0,
        };
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
    fn test_fake_mem_metric_returns_configured_value() {
        let metric = FakeMemMetric::new(16_000_000_000, 8_000_000_000);
        let mem = metric.get();
        assert_eq!(mem.total, 16_000_000_000);
        assert_eq!(mem.used, 8_000_000_000);
        assert_eq!(mem.percent, 50.0);
    }

    #[test]
    fn test_mem_metric_returns_valid_values() {
        let metric = MemMetric::new();
        let mem = metric.get();
        assert!(mem.total > 0);
        assert!(mem.used <= mem.total);
        assert!(mem.percent >= 0.0 && mem.percent <= 100.0);
    }

    #[test]
    fn test_mem_metric_refresh() {
        let mut metric = MemMetric::new();
        metric.refresh();
        let mem = metric.get();
        assert!(mem.total > 0);
        assert!(mem.percent >= 0.0 && mem.percent <= 100.0);
    }
}
