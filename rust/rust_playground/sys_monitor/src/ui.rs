use ratatui::{
    layout::{Constraint, Layout, Rect},
    style::{Color, Style, Stylize},
    widgets::{Block, Borders, Gauge, Row, Table},
    Frame,
};

use crate::metrics::{CpuInfo, DiskInfo, MemInfo};

// ============== Drawable Trait ==============

pub trait Drawable {
    fn draw(&self, frame: &mut Frame, area: Rect);
    fn height(&self) -> u16;
}

// ============== CpuInfo Drawable (per-core) ==============

impl Drawable for CpuInfo {
    fn draw(&self, frame: &mut Frame, area: Rect) {
        let rows: Vec<Row> = self
            .cores
            .iter()
            .map(|core| {
                let bar = create_bar(core.usage as u16, 20);
                Row::new(vec![
                    core.name.clone(),
                    bar,
                    format!("{:5.1}%", core.usage),
                ])
            })
            .collect();

        let table = Table::new(
            rows,
            [
                Constraint::Length(8),    // Core name
                Constraint::Length(22),   // Bar
                Constraint::Length(8),    // Percent
            ],
        )
        .block(
            Block::default()
                .title(format!(" CPU (avg: {:.1}%) ", self.avg_usage))
                .borders(Borders::ALL),
        );

        frame.render_widget(table, area);
    }

    fn height(&self) -> u16 {
        // 2 for borders + 1 per core
        (self.cores.len() as u16).saturating_add(2).max(3)
    }
}

// ============== MemInfo Drawable ==============

impl Drawable for MemInfo {
    fn draw(&self, frame: &mut Frame, area: Rect) {
        let used_gb = self.used as f64 / 1_000_000_000.0;
        let total_gb = self.total as f64 / 1_000_000_000.0;

        let gauge = Gauge::default()
            .block(Block::default().title(" Memory ").borders(Borders::ALL))
            .gauge_style(Style::default().fg(Color::Green))
            .percent(self.percent as u16)
            .label(format!(
                "{:.1} GB / {:.1} GB ({:.1}%)",
                used_gb, total_gb, self.percent
            ));
        frame.render_widget(gauge, area);
    }

    fn height(&self) -> u16 {
        3
    }
}

// ============== Vec<DiskInfo> Drawable ==============

impl Drawable for Vec<DiskInfo> {
    fn draw(&self, frame: &mut Frame, area: Rect) {
        let rows: Vec<Row> = self
            .iter()
            .map(|d| {
                let used_gb = d.used as f64 / 1_000_000_000.0;
                let total_gb = d.total as f64 / 1_000_000_000.0;
                let bar = create_bar(d.percent as u16, 20);
                Row::new(vec![
                    d.name.clone(),
                    format!("{:.1} GB", used_gb),
                    format!("{:.1} GB", total_gb),
                    bar,
                    format!("{:.1}%", d.percent),
                ])
            })
            .collect();

        let table = Table::new(
            rows,
            [
                Constraint::Min(15),      // Mount point
                Constraint::Length(10),   // Used
                Constraint::Length(10),   // Total
                Constraint::Length(22),   // Bar
                Constraint::Length(8),    // Percent
            ],
        )
        .header(
            Row::new(vec!["Mount", "Used", "Total", "Usage", "%"]).style(Style::default().bold()),
        )
        .block(Block::default().title(" Disks ").borders(Borders::ALL));

        frame.render_widget(table, area);
    }

    fn height(&self) -> u16 {
        // 2 for borders + 1 for header + 1 per disk
        (self.len() as u16).saturating_add(3).max(5)
    }
}

// ============== Helper ==============

fn create_bar(percent: u16, width: usize) -> String {
    let percent = percent.min(100);
    let filled = (percent as usize * width) / 100;
    let empty = width - filled;
    format!("[{}{}]", "█".repeat(filled), "░".repeat(empty))
}

// ============== Main Draw Function ==============

pub fn draw(frame: &mut Frame, items: &[&dyn Drawable]) {
    let constraints: Vec<Constraint> = items
        .iter()
        .enumerate()
        .map(|(i, item)| {
            if i < items.len() - 1 {
                Constraint::Length(item.height())
            } else {
                Constraint::Min(item.height())
            }
        })
        .collect();

    let areas = Layout::vertical(constraints).split(frame.area());

    for (i, item) in items.iter().enumerate() {
        item.draw(frame, areas[i]);
    }
}

// ============== Tests ==============

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_create_bar_empty() {
        assert_eq!(create_bar(0, 10), "[░░░░░░░░░░]");
    }

    #[test]
    fn test_create_bar_full() {
        assert_eq!(create_bar(100, 10), "[██████████]");
    }

    #[test]
    fn test_create_bar_half() {
        assert_eq!(create_bar(50, 10), "[█████░░░░░]");
    }
}
