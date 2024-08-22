package com.example.my_first_rest_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/todo")
    public ResponseEntity<Todo> get(@RequestParam(value = "id") int id){

        //Eintrag aus DB per ID holen
        Optional<Todo> todoInDb = todoRepository.findById(id);

        if (todoInDb.isPresent()){
            return new ResponseEntity<Todo>(todoInDb.get(), HttpStatus.OK); //Eintrag wurde gefunden
        }

        return new ResponseEntity("Not found", HttpStatus.NOT_FOUND); //Eintrag existiert nicht

    }

    //Alle Einträge aus der DB holen
    @GetMapping("/todo/all")
    public ResponseEntity<Iterable<Todo>> getAll(){
        Iterable<Todo> allTodoInDb = todoRepository.findAll();

        return new ResponseEntity<Iterable<Todo>>(allTodoInDb, HttpStatus.OK);
    }

    @PostMapping("/todo")
    public ResponseEntity<Todo> create(@RequestBody Todo newTodo){
        // Über Request Body mitgegebene Todo Entity in DB speichern
        todoRepository.save(newTodo);
        return new ResponseEntity<Todo>(newTodo, HttpStatus.OK);

    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam(value = "id") int id){

        //Zu löschenden Eintrag aus DB holen
        Optional<Todo> todoInDb = todoRepository.findById(id);

        //Prüfen ob Eintrag exisitert
        if (todoInDb.isPresent()){
            todoRepository.deleteById(id); //Eintrag löschen

            return new ResponseEntity("Eintrag " + id + " wurde gelöscht", HttpStatus.OK);
        }

        //Eintrag existiert nicht, Fehler zurückgeben
        return new ResponseEntity("Eintrag nicht gefunden", HttpStatus.NOT_FOUND);

    }

    @PutMapping("/Todo/Edit")
    public ResponseEntity<Todo> edit(@RequestBody Todo editTodo){

        //Eintrag holen
        Optional<Todo> todoInDb = todoRepository.findById(editTodo.getId());

        //Eintrag auf existenz prüfen
        if (todoInDb.isPresent()){

            //Eintrag mit dem im Request Body mitgebenen Eintrag "austauschen"
            todoRepository.save(editTodo);

            return new ResponseEntity("Eintrag " + editTodo.getId() + " wurde geändert", HttpStatus.OK);

        }
        //Eintrag existiert nicht
        return new ResponseEntity("Eintrag nicht gefunden", HttpStatus.NOT_FOUND);

    }
    //Task auf completed setzen
    @PatchMapping("/Todo/Completed")
    public ResponseEntity completeTask( @RequestParam(value ="isDone") boolean isDone,
                                        @RequestParam(value = "id") int id){

        //Eintrag holen
        Optional<Todo> todoInDb = todoRepository.findById(id);

        //Auf existenz prüfen
        if (todoInDb.isPresent()){
            todoInDb.get().setDone(isDone); //Wert isDone ändern
            Todo saveTodo = todoRepository.save(todoInDb.get());
            return new ResponseEntity<Todo>(saveTodo, HttpStatus.OK);
        }

        return new ResponseEntity("Fehler", HttpStatus.BAD_REQUEST);

    }

}
