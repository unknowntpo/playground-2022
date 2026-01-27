use sysinfo::Disks;
use super::Metric;

// ============== Data Struct ==============

#[derive(Debug, Clone, PartialEq)]
pub struct DiskInfo {
    pub name: String,
    pub total: u64,
    pub used: u64,
    pub percent: f32,
}

// ============== Fake Implementation ==============

pub struct FakeDiskMetric {
    value: Vec<DiskInfo>,
}

impl FakeDiskMetric {
    pub fn new(disks: Vec<DiskInfo>) -> Self {
        Self { value: disks }
    }

    pub fn single(name: &str, total: u64, used: u64) -> Self {
        let percent = (used as f32 / total as f32) * 100.0;
        Self {
            value: vec![DiskInfo {
                name: name.to_string(),
                total,
                used,
                percent,
            }],
        }
    }
}

impl Metric for FakeDiskMetric {
    type Output = Vec<DiskInfo>;

    fn refresh(&mut self) {
        // no-op for fake
    }

    fn get(&self) -> Self::Output {
        self.value.clone()
    }
}

// ============== Real Implementation (sysinfo) ==============

pub struct DiskMetric {
    disks: Disks,
    value: Vec<DiskInfo>,
}

impl DiskMetric {
    pub fn new() -> Self {
        let disks = Disks::new_with_refreshed_list();
        let value = Self::collect_disk_info(&disks);
        Self { disks, value }
    }

    fn collect_disk_info(disks: &Disks) -> Vec<DiskInfo> {
        disks
            .iter()
            .filter(|d| {
                let mount = d.mount_point().to_string_lossy();
                // Filter out macOS system volumes that cause warnings
                !mount.starts_with("/System/Volumes")
                    && mount != "/dev"
                    && d.total_space() > 0
            })
            .map(|d| {
                let total = d.total_space();
                let used = total - d.available_space();
                let percent = (used as f32 / total as f32) * 100.0;
                DiskInfo {
                    name: d.mount_point().to_string_lossy().to_string(),
                    total,
                    used,
                    percent,
                }
            })
            .collect()
    }
}

impl Default for DiskMetric {
    fn default() -> Self {
        Self::new()
    }
}

impl Metric for DiskMetric {
    type Output = Vec<DiskInfo>;

    fn refresh(&mut self) {
        self.disks.refresh();
        self.value = Self::collect_disk_info(&self.disks);
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
    fn test_fake_disk_metric_single() {
        let metric = FakeDiskMetric::single("/", 500_000_000_000, 250_000_000_000);
        let disks = metric.get();
        assert_eq!(disks.len(), 1);
        assert_eq!(disks[0].name, "/");
        assert_eq!(disks[0].percent, 50.0);
    }

    #[test]
    fn test_disk_metric_returns_disks() {
        let metric = DiskMetric::new();
        let disks = metric.get();
        // Should have at least one disk on any system
        assert!(!disks.is_empty());
    }

    #[test]
    fn test_disk_metric_valid_values() {
        let metric = DiskMetric::new();
        let disks = metric.get();
        for disk in disks {
            assert!(disk.total > 0);
            assert!(disk.used <= disk.total);
            assert!(disk.percent >= 0.0 && disk.percent <= 100.0);
        }
    }

    #[test]
    fn test_disk_metric_refresh() {
        let mut metric = DiskMetric::new();
        metric.refresh();
        let disks = metric.get();
        println!("{:#?}", disks);
        assert!(!disks.is_empty());
    }
}
