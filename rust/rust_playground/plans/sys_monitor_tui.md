# System Monitor TUI Dashboard

## Goal
Real-time CPU/Memory/Disk metrics in terminal dashboard.

## Crates
- `sysinfo` - cross-platform system metrics
- `ratatui` - TUI framework (modern tui-rs fork)
- `crossterm` - terminal backend

## Tasks

### 1. Metrics collection
- [ ] Metric trait
  - method: refresh(&mut self)
  - get(&self) -> T (e.g. CpuInfo, MemInfo, Vec<DiskInfo>)
- [ ] FakeCpuMetric
- [ ] FakeMemMetric
- [ ] FakeDiskMetric
- [ ] CPUMetric(sysinfo)
- [ ] MemoryMetric (sysinfo)
- [ ] DiskMetric (sysinfo)
- [ ] MetricsSource: contains collection of metrics
- [ ] Spec: when creating a MetricsSource with poll interval(5ms). it should return metrics every 5ms.
- [ ] Integration test on macOS/Linux (optional)

### 2. TUI layout
- [ ] Draw UI with hardcoded data
- [ ] Swap in `MetricsSource` trait

### 3. Main loop
- [ ] Terminal setup (raw mode, alternate screen)
- [ ] Poll input (crossterm events, 'q' to quit)
- [ ] Refresh + draw loop (250ms interval)
- [ ] Cleanup on exit (restore terminal)

### Dependency
```
Metrics (trait + mock) → TUI (hardcoded) → Main loop → Integrate all
```

## Class Diagram

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           <<trait>> Metric<T>                           │
├─────────────────────────────────────────────────────────────────────────┤
│  + refresh(&mut self)                                                   │
│  + get(&self) -> T                                                      │
└─────────────────────────────────────────────────────────────────────────┘
                                    ▲
                                    │ impl
        ┌───────────────────────────┼───────────────────────────┐
        │                           │                           │
        │                           │                           │
┌───────┴───────┐           ┌───────┴───────┐           ┌───────┴───────┐
│  CpuMetric    │           │  MemMetric    │           │  DiskMetric   │
├───────────────┤           ├───────────────┤           ├───────────────┤
│ Metric<       │           │ Metric<       │           │ Metric<       │
│   CpuInfo>    │           │   MemInfo>    │           │ Vec<DiskInfo>>│
└───────┬───────┘           └───────┬───────┘           └───────┬───────┘
        │                           │                           │
        │ impl                      │ impl                      │ impl
   ┌────┴────┐                 ┌────┴────┐                 ┌────┴────┐
   │         │                 │         │                 │         │
┌──┴───┐ ┌───┴──┐          ┌───┴──┐ ┌────┴───┐         ┌───┴──┐ ┌────┴───┐
│ Fake │ │Real  │          │ Fake │ │ Real   │         │ Fake │ │ Real   │
│ Cpu  │ │Cpu   │          │ Mem  │ │ Mem    │         │ Disk │ │ Disk   │
│Metric│ │Metric│          │Metric│ │ Metric │         │Metric│ │ Metric │
└──────┘ └──────┘          └──────┘ └────────┘         └──────┘ └────────┘
           │                          │                           │
           └──────────────────────────┼───────────────────────────┘
                                      │ uses
                                      ▼
                               ┌─────────────┐
                               │   sysinfo   │
                               └─────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                            MetricsSource                                │
├─────────────────────────────────────────────────────────────────────────┤
│  - cpu: Box<dyn Metric<CpuInfo>>                                        │
│  - mem: Box<dyn Metric<MemInfo>>                                        │
│  - disk: Box<dyn Metric<Vec<DiskInfo>>>                                 │
│  - poll_interval: Duration                                              │
├─────────────────────────────────────────────────────────────────────────┤
│  + new(cpu, mem, disk, interval) -> Self                                │
│  + refresh(&mut self)                                                   │
│  + cpu(&self) -> CpuInfo                                                │
│  + memory(&self) -> MemInfo                                             │
│  + disks(&self) -> Vec<DiskInfo>                                        │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                              DATA STRUCTS                               │
├─────────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────┐  ┌─────────────────────┐  ┌─────────────────────┐  │
│  │    CpuInfo      │  │     MemInfo         │  │     DiskInfo        │  │
│  ├─────────────────┤  ├─────────────────────┤  ├─────────────────────┤  │
│  │ + usage: f32    │  │ + total: u64        │  │ + name: String      │  │
│  │                 │  │ + used: u64         │  │ + total: u64        │  │
│  │                 │  │ + percent: f32      │  │ + used: u64         │  │
│  │                 │  │                     │  │ + percent: f32      │  │
│  └─────────────────┘  └─────────────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────────────────┘
```

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                           TERMINAL                              │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  CPU [████████░░░░░░] 52%    Memory [██████████░░] 78%    │  │
│  │  Disk /  [████████████████░░░░] 85%                       │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              ▲
                              │ render
┌─────────────────────────────┴───────────────────────────────────┐
│                        APPLICATION                              │
│                                                                 │
│  ┌─────────────────┐   ┌─────────────────┐   ┌───────────────┐  │
│  │    main.rs      │   │     ui.rs       │   │  metrics.rs   │  │
│  │                 │   │                 │   │               │  │
│  │  - setup        │──▶│  - draw()       │◀──│  - refresh()  │  │
│  │  - loop         │   │  - layout       │   │  - cpu()      │  │
│  │  - input        │   │                 │   │  - memory()   │  │
│  │                 │   │  uses:          │   │  - disks()    │  │
│  │  uses:          │   │  ┌───────────┐  │   │               │  │
│  │  ┌───────────┐  │   │  │ ratatui   │  │   │  uses:        │  │
│  │  │ crossterm │  │   │  │ - Gauge   │  │   │  ┌─────────┐  │  │
│  │  │ - raw mode│  │   │  │ - Layout  │  │   │  │ sysinfo │  │  │
│  │  │ - events  │  │   │  └───────────┘  │   │  └────┬────┘  │  │
│  │  └───────────┘  │   └─────────────────┘   └───────┼──────┘  │
│  └─────────────────┘                                 │         │
└──────────────────────────────────────────────────────┼─────────┘
                                                       │ syscall
┌──────────────────────────────────────────────────────┼─────────┐
│                   macOS / Linux KERNEL               ▼         │
│                                                                │
│   /proc/stat    /proc/meminfo    /sys/block/*    sysctl (mac)  │
└────────────────────────────────────────────────────────────────┘
```

## Data Flow (Simple Polling)

```
┌────────────────────────────────────────────────────────────────┐
│                       main.rs LOOP                             │
│                                                                │
│   loop {                                                       │
│       ┌──────────────┐                                         │
│       │  poll input  │ ◀─── crossterm::event::poll (250ms)     │
│       └──────┬───────┘                                         │
│              │                                                 │
│              ▼                                                 │
│       ┌──────────────┐      syscall                            │
│       │   refresh    │ ─────────────▶ KERNEL                   │
│       │   metrics    │ ◀───────────── /proc, sysctl            │
│       └──────┬───────┘                                         │
│              │                                                 │
│              ▼                                                 │
│       ┌──────────────┐                                         │
│       │   draw UI    │ ◀─── ratatui widgets                    │
│       └──────┬───────┘                                         │
│              │                                                 │
│              ▼                                                 │
│          TERMINAL                                              │
│   }                                                            │
└────────────────────────────────────────────────────────────────┘
```

## Setup
```bash
cargo new sys_monitor
cd sys_monitor
cargo add sysinfo ratatui crossterm
```

## Phase 1: Metrics Collection (TDD)

### Tests
```rust
// src/metrics.rs
#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_cpu_usage_in_range() {
        let metrics = SystemMetrics::new();
        let cpu = metrics.cpu_usage();
        assert!(cpu >= 0.0 && cpu <= 100.0);
    }

    #[test]
    fn test_memory_usage() {
        let metrics = SystemMetrics::new();
        let mem = metrics.memory();
        assert!(mem.used <= mem.total);
    }

    #[test]
    fn test_disk_usage() {
        let metrics = SystemMetrics::new();
        let disks = metrics.disks();
        assert!(!disks.is_empty());
    }
}
```

### Implementation
```rust
// src/metrics.rs
use sysinfo::{System, Disks};

pub struct MemoryInfo {
    pub total: u64,
    pub used: u64,
    pub percent: f32,
}

pub struct DiskInfo {
    pub name: String,
    pub total: u64,
    pub used: u64,
    pub percent: f32,
}

pub struct SystemMetrics {
    sys: System,
}

impl SystemMetrics {
    pub fn new() -> Self {
        Self { sys: System::new_all() }
    }

    pub fn refresh(&mut self) {
        self.sys.refresh_all();
    }

    pub fn cpu_usage(&self) -> f32 {
        self.sys.global_cpu_usage()
    }

    pub fn memory(&self) -> MemoryInfo {
        let total = self.sys.total_memory();
        let used = self.sys.used_memory();
        MemoryInfo {
            total,
            used,
            percent: (used as f32 / total as f32) * 100.0,
        }
    }

    pub fn disks(&self) -> Vec<DiskInfo> {
        let disks = Disks::new_with_refreshed_list();
        disks.iter().map(|d| {
            let total = d.total_space();
            let used = total - d.available_space();
            DiskInfo {
                name: d.name().to_string_lossy().to_string(),
                total,
                used,
                percent: (used as f32 / total as f32) * 100.0,
            }
        }).collect()
    }
}
```

## Phase 2: TUI Layout

### Basic Structure
```rust
// src/ui.rs
use ratatui::{
    layout::{Constraint, Direction, Layout},
    widgets::{Block, Borders, Gauge, Paragraph},
    Frame,
};

pub fn draw(frame: &mut Frame, metrics: &SystemMetrics) {
    let chunks = Layout::default()
        .direction(Direction::Vertical)
        .margin(1)
        .constraints([
            Constraint::Length(3),  // CPU
            Constraint::Length(3),  // Memory
            Constraint::Min(5),     // Disks
        ])
        .split(frame.area());

    // CPU Gauge
    let cpu_gauge = Gauge::default()
        .block(Block::default().title("CPU").borders(Borders::ALL))
        .percent(metrics.cpu_usage() as u16);
    frame.render_widget(cpu_gauge, chunks[0]);

    // Memory Gauge
    let mem = metrics.memory();
    let mem_gauge = Gauge::default()
        .block(Block::default().title("Memory").borders(Borders::ALL))
        .percent(mem.percent as u16);
    frame.render_widget(mem_gauge, chunks[1]);

    // Disks
    let disk_block = Block::default().title("Disks").borders(Borders::ALL);
    frame.render_widget(disk_block, chunks[2]);
}
```

## Phase 3: Main Loop

```rust
// src/main.rs
mod metrics;
mod ui;

use std::{io, time::Duration};
use crossterm::{
    event::{self, Event, KeyCode},
    terminal::{disable_raw_mode, enable_raw_mode, EnterAlternateScreen, LeaveAlternateScreen},
    execute,
};
use ratatui::{backend::CrosstermBackend, Terminal};

fn main() -> io::Result<()> {
    // Setup terminal
    enable_raw_mode()?;
    let mut stdout = io::stdout();
    execute!(stdout, EnterAlternateScreen)?;
    let backend = CrosstermBackend::new(stdout);
    let mut terminal = Terminal::new(backend)?;

    // App state
    let mut metrics = metrics::SystemMetrics::new();

    // Main loop
    loop {
        metrics.refresh();
        terminal.draw(|f| ui::draw(f, &metrics))?;

        // Handle input (q to quit)
        if event::poll(Duration::from_millis(250))? {
            if let Event::Key(key) = event::read()? {
                if key.code == KeyCode::Char('q') {
                    break;
                }
            }
        }
    }

    // Restore terminal
    disable_raw_mode()?;
    execute!(terminal.backend_mut(), LeaveAlternateScreen)?;
    Ok(())
}
```

## Phase 4: Enhancements

### Add Sparkline for CPU history
```rust
use ratatui::widgets::Sparkline;

pub struct App {
    cpu_history: Vec<u64>,  // last 60 readings
}

impl App {
    pub fn update(&mut self, cpu: f32) {
        self.cpu_history.push(cpu as u64);
        if self.cpu_history.len() > 60 {
            self.cpu_history.remove(0);
        }
    }
}

// In draw():
let sparkline = Sparkline::default()
    .block(Block::default().title("CPU History"))
    .data(&app.cpu_history);
```

## Run
```bash
cargo run
# Press 'q' to quit
```

## File Structure
```
src/
├── main.rs      # Terminal setup, main loop
├── metrics.rs   # System metrics collection
├── ui.rs        # TUI drawing
└── app.rs       # App state (optional)
```

## Screenshot (ASCII)
```
┌─ CPU ──────────────────────────────┐
│████████████░░░░░░░░░░░░░░░░░ 42%   │
└────────────────────────────────────┘
┌─ Memory ───────────────────────────┐
│██████████████████░░░░░░░░░░░ 65%   │
└────────────────────────────────────┘
┌─ Disks ────────────────────────────┐
│ /dev/sda1  450GB / 500GB  (90%)    │
│ /dev/sdb1  100GB / 1TB    (10%)    │
└────────────────────────────────────┘
```
