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
}

// ============== CpuInfo Drawable ==============

impl Drawable for CpuInfo {
    fn draw(&self, frame: &mut Frame, area: Rect) {
        let gauge = Gauge::default()
            .block(Block::default().title(" CPU ").borders(Borders::ALL))
            .gauge_style(Style::default().fg(Color::Cyan))
            .percent(self.usage as u16)
            .label(format!("{:.1}%", self.usage));
        frame.render_widget(gauge, area);
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
            .label(format!("{:.1} GB / {:.1} GB ({:.1}%)", used_gb, total_gb, self.percent));
        frame.render_widget(gauge, area);
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
            Row::new(vec!["Mount", "Used", "Total", "Usage", "%"])
                .style(Style::default().bold()),
        )
        .block(Block::default().title(" Disks ").borders(Borders::ALL));

        frame.render_widget(table, area);
    }
}

// ============== Helper ==============

fn create_bar(percent: u16, width: usize) -> String {
    let filled = (percent as usize * width) / 100;
    let empty = width - filled;
    format!("[{}{}]", "█".repeat(filled), "░".repeat(empty))
}

// ============== Main Draw Function ==============

pub fn draw(frame: &mut Frame, items: &[&dyn Drawable]) {
    let constraints: Vec<Constraint> = items
        .iter()
        .enumerate()
        .map(|(i, _)| {
            if i < items.len() - 1 {
                Constraint::Length(3) // Fixed height for gauges
            } else {
                Constraint::Min(5) // Last item (disks) takes remaining space
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
