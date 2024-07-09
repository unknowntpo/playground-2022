trait BaseTask<T> {
    type Error;

    fn run(&self) -> Result<(), Self::Error> {
        self.prepare_all().and_then(|tasks| {
            for task in tasks {
                match self.process_one(task) {
                    Ok(_) => println!("ok"),
                    Err(_) => println!("Failed"),
                }
            }
            Ok(())
        })?;
        Ok(())
    }
    fn prepare_all(&self) -> Result<Vec<T>, Self::Error>;
    fn process_one(&self, t: T) -> Result<(), Self::Error>;
}

#[derive(Debug)]
struct TaskError;

trait BaseRoutineTask: BaseTask<i32> {
    // type Error = TaskError;

    fn fetch(&self) -> Result<i32, Self::Error>;
    fn verify(&self, t: i32) -> Result<(), Self::Error>;

    fn prepare_all(&self) -> Result<Vec<i32>, Self::Error> {
        Ok(vec![1i32, 2, 3])
    }
    fn process_one(&self, t: i32) -> Result<(), Self::Error> {
        println!("{}", t);
        self.fetch().and_then(|i| self.verify(i))
    }
}

struct UrlFetchTask {
    payload: i32,
}

impl BaseRoutineTask for UrlFetchTask {
    // type Error = TaskError;

    fn fetch(&self) -> Result<i32, Self::Error> {
        Ok(3i32)
    }

    fn verify(&self, i: i32) -> Result<(), Self::Error> {
        Ok(())
    }
}

impl BaseTask<i32> for UrlFetchTask {
    type Error = TaskError;

    fn prepare_all(&self) -> Result<Vec<i32>, Self::Error> {
        BaseRoutineTask::prepare_all(self)
    }

    fn process_one(&self, t: i32) -> Result<(), Self::Error> {
        BaseRoutineTask::process_one(self, t)
    }
}

#[cfg(test)]
mod test {
    use super::*;
    #[test]
    fn test_task() {
        let task = UrlFetchTask { payload: 42 };
        if let Err(e) = task.run() {
            println!("Task failed: {:?}", e);
        }
    }
}
