# System Monitor TUI Dashboard

## Goal
Real-time CPU/Memory/Disk metrics in terminal dashboard with per-core CPU display.

## Crates
- `sysinfo` - cross-platform system metrics
- `ratatui` - TUI framework
- `crossterm` - terminal backend

## Tasks

### 1. Metrics collection
- [x] Metric trait (associated type Output)
- [x] FakeCpuMetric (per-core)
- [x] FakeMemMetric
- [x] FakeDiskMetric
- [x] CpuMetric (sysinfo, per-core)
- [x] MemMetric (sysinfo)
- [x] DiskMetric (sysinfo, filters macOS system volumes)
- [x] MetricsSource aggregator (dependency injection)

### 2. TUI layout
- [x] Drawable trait (self-rendering)
- [x] CpuInfo Drawable (per-core table)
- [x] MemInfo Drawable (gauge)
- [x] Vec<DiskInfo> Drawable (table)
- [x] Dynamic layout via height()

### 3. Main loop
- [x] Terminal setup (raw mode, alternate screen)
- [x] Poll input ('q' to quit)
- [x] Refresh + draw loop (250ms)
- [x] Cleanup on exit

## Class Diagram

```
┌─────────────────────────────────────────────────────────────────────────┐
│                      <<trait>> Metric                                   │
├─────────────────────────────────────────────────────────────────────────┤
│  type Output                                                            │
│  + refresh(&mut self)                                                   │
│  + get(&self) -> Self::Output                                           │
└─────────────────────────────────────────────────────────────────────────┘
                                   ▲
                                   │ impl
       ┌───────────────────────────┼───────────────────────────┐
       │                           │                           │
┌──────┴──────┐            ┌───────┴───────┐           ┌───────┴───────┐
│ CpuMetric   │            │  MemMetric    │           │  DiskMetric   │
│ FakeCpu     │            │  FakeMem      │           │  FakeDisk     │
└─────────────┘            └───────────────┘           └───────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                           MetricsSource                                 │
├─────────────────────────────────────────────────────────────────────────┤
│  - cpu: Box<dyn Metric<Output = CpuInfo>>                               │
│  - mem: Box<dyn Metric<Output = MemInfo>>                               │
│  - disk: Box<dyn Metric<Output = Vec<DiskInfo>>>                        │
├─────────────────────────────────────────────────────────────────────────┤
│  + with_real_metrics() -> Self                                          │
│  + with_fake_metrics(...) -> Self                                       │
│  + refresh(&mut self)                                                   │
│  + cpu() / mem() / disks()                                              │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                           <<trait>> Drawable                            │
├─────────────────────────────────────────────────────────────────────────┤
│  + draw(&self, frame: &mut Frame, area: Rect)                           │
│  + height(&self) -> u16                                                 │
└─────────────────────────────────────────────────────────────────────────┘
       ▲ impl: CpuInfo, MemInfo, Vec<DiskInfo>

┌─────────────────────────────────────────────────────────────────────────┐
│                              DATA STRUCTS                               │
├─────────────────────────────────────────────────────────────────────────┤
│  CoreInfo { name, usage }                                               │
│  CpuInfo  { cores: Vec<CoreInfo>, avg_usage }                           │
│  MemInfo  { total, used, percent }                                      │
│  DiskInfo { name, total, used, percent }                                │
└─────────────────────────────────────────────────────────────────────────┘
```

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                           TERMINAL                              │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  CPU (avg: 45.2%)                                         │  │
│  │  Core 0 [████████░░░░] 38.5%                              │  │
│  │  Core 1 [██████████░░] 52.1%                              │  │
│  ├───────────────────────────────────────────────────────────┤  │
│  │  Memory  [████████████░░░░] 8.2 GB / 16.0 GB (51.2%)      │  │
│  ├───────────────────────────────────────────────────────────┤  │
│  │  Disks   /  250 GB / 500 GB [████████░░░░] 50%            │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              ▲ render
┌─────────────────────────────┴───────────────────────────────────┐
│  main.rs ──▶ ui.rs (Drawable) ◀── metrics/ (Metric trait)       │
│     │              │                    │                       │
│  crossterm      ratatui              sysinfo                    │
└─────────────────────────────┬───────────────────────────────────┘
                              │ syscall
                              ▼
                    macOS / Linux KERNEL
```

## File Structure
```
sys_monitor/
├── Cargo.toml
└── src/
    ├── lib.rs
    ├── main.rs
    ├── ui.rs
    └── metrics/
        ├── mod.rs
        ├── cpu.rs
        ├── mem.rs
        ├── disk.rs
        └── source.rs
```

## Run
```bash
cargo run -p sys_monitor
# Press 'q' to quit
```
