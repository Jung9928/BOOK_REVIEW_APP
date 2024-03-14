package com.jung9407.bookreviewapp.service;

import com.jung9407.bookreviewapp.model.dto.responseDTO.BookResponseDTO;
import com.jung9407.bookreviewapp.model.entity.BookEntity;
import com.jung9407.bookreviewapp.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private static final int BAR_LENGTH = 5;

//    public List<BookResponseDTO> findAllBooks() {
//        List<BookEntity> bookEntitiesList = bookRepository.findAll();
//        List<BookResponseDTO> bookResponseDTOList = new ArrayList<>();
//
//        for (BookEntity bookEntity : bookEntitiesList) {
//            bookResponseDTOList.add(BookResponseDTO.entityToBookResponseDTO(bookEntity));
//        }
//
//        return bookResponseDTOList;
//    }

    //
    @Transactional(readOnly = true)
    public Page<BookResponseDTO> findBookList(String searchKeyword, Pageable pageable) {
        if(searchKeyword == null || searchKeyword.isBlank()) {
            return bookRepository.findAll(pageable).map(BookResponseDTO::entityToBookResponseDTO);
        }

        return bookRepository.findAllByTitle(searchKeyword, pageable).map(BookResponseDTO::entityToBookResponseDTO);
//        return bookRepository.findAll(pageable).map(BookResponseDTO::entityToBookResponseDTO);
    }

//    public Page<BookResponseDTO> paging(Pageable pageable) {
//        int page = pageable.getPageNumber() - 1;    // PageRequest.of 메소드의 인자인 page 인덱스는 0부터 시작하므로 -1 처리
//        int pageLimit = 15;                         // 한 페이지에 보여줄 도서 정보 갯수
//
//        // 한 페이지당 15개씩 도서 정보를 보여주고 정렬 기준은 book_id 기준으로 오름차순 정렬
//        Page<BookEntity> bookEntities =
//                bookRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.ASC, "book_id")));
//
//        System.out.println("bookEntities.getContent() : " + bookEntities.getContent());             // 요청 페이지에 해당하는 도서정보
//        System.out.println("bookEntities.getTotalElements() : " + bookEntities.getTotalElements()); // 전체 도서 정보
//        System.out.println("bookEntities.getNumber() : " + bookEntities.getNumber());               // DB로 요청한 페이지 번호
//        System.out.println("bookEntities.getTotalPages() : " + bookEntities.getTotalPages());       // 전체 페이지 갯수
//        System.out.println("bookEntities.getSize() : " + bookEntities.getSize());                   // 한 페이지에 보여지는 도서정보 갯수
//        System.out.println("bookEntities.hasPrevious() : " + bookEntities.hasPrevious());           // 이전 페이지 존재 여부
//        System.out.println("bookEntities.hasNext() : " + bookEntities.hasNext());                   // 다음 페이지 존재 여부
//        System.out.println("bookEntities.isFirst() : " + bookEntities.isFirst());                   // 첫 페이지 여부
//        System.out.println("bookEntities.isLast() : " + bookEntities.isLast());                     // 마지막 페이지 여부
//
//        // book 엔티티 객체 데이터들을 dto에 저장
//        Page<BookResponseDTO> bookResponseDTOS = bookEntities.map(bookEntity -> new BookResponseDTO(
//                bookEntity.getBook_id(),
//                bookEntity.getTitle(),
//                bookEntity.getAuthor(),
//                bookEntity.getPublisher(),
//                bookEntity.getPublishDate(),
//                bookEntity.getRating(),
//                bookEntity.getOrigin_price(),
//                bookEntity.getSale_price(),
//                bookEntity.getImgPath(),
//                bookEntity.getModifiedAt(),
//                bookEntity.getSite()
//        ));
//    }

    // Pagination bar
    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 0);
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages);

        return IntStream.range(startNumber, endNumber).boxed().toList();
    }

    public int currentBarLength() {
        return BAR_LENGTH;
    }
}
