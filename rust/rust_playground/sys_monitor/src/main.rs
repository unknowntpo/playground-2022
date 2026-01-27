use std::{io, time::Duration};

use crossterm::{
    event::{self, Event, KeyCode},
    execute,
    terminal::{disable_raw_mode, enable_raw_mode, EnterAlternateScreen, LeaveAlternateScreen},
};
use ratatui::{backend::CrosstermBackend, Terminal};

use sys_monitor::metrics::MetricsSource;
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
    // Use real system metrics
    let mut source = MetricsSource::with_real_metrics();

    loop {
        // Refresh metrics
        source.refresh();

        // Draw UI
        terminal.draw(|frame| {
            let cpu = source.cpu().clone();
            let mem = source.mem().clone();
            let disks = source.disks().clone();

            let items: Vec<&dyn Drawable> = vec![&cpu, &mem, &disks];
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
