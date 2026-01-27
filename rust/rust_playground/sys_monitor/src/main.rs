use std::{io, time::Duration};

use crossterm::{
    event::{self, Event, KeyCode},
    execute,
    terminal::{disable_raw_mode, enable_raw_mode, EnterAlternateScreen, LeaveAlternateScreen},
};
use ratatui::{backend::CrosstermBackend, Terminal};

use sys_monitor::metrics::{CpuInfo, DiskInfo, MemInfo};
use sys_monitor::ui::{self, Drawable};

fn main() -> io::Result<()> {
    // Setup terminal
    enable_raw_mode()?;
    let mut stdout = io::stdout();
    execute!(stdout, EnterAlternateScreen)?;
    let backend = CrosstermBackend::new(stdout);
    let mut terminal = Terminal::new(backend)?;

    // Run app
    let result = run_app(&mut terminal);

    // Restore terminal
    disable_raw_mode()?;
    execute!(terminal.backend_mut(), LeaveAlternateScreen)?;

    result
}

fn run_app(terminal: &mut Terminal<CrosstermBackend<io::Stdout>>) -> io::Result<()> {
    // Hardcoded data for now
    let cpu_info = CpuInfo { usage: 45.5 };
    let mem_info = MemInfo {
        total: 16_000_000_000,
        used: 10_400_000_000,
        percent: 65.0,
    };
    let disk_info: Vec<DiskInfo> = vec![
        DiskInfo {
            name: "/".to_string(),
            total: 500_000_000_000,
            used: 350_000_000_000,
            percent: 70.0,
        },
        DiskInfo {
            name: "/home".to_string(),
            total: 1_000_000_000_000,
            used: 200_000_000_000,
            percent: 20.0,
        },
    ];

    loop {
        // Draw UI
        terminal.draw(|frame| {
            let items: Vec<&dyn Drawable> = vec![&cpu_info, &mem_info, &disk_info];
            ui::draw(frame, &items);
        })?;

        // Handle input (poll every 250ms)
        if event::poll(Duration::from_millis(250))? {
            if let Event::Key(key) = event::read()? {
                if key.code == KeyCode::Char('q') {
                    break;
                }
            }
        }
    }

    Ok(())
}
