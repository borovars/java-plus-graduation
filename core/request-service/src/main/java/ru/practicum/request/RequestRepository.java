package ru.practicum.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.feign.request.enums.RequestStatus;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Page<Request> findAllByRequester(Long userId, Pageable pageable);

    boolean existsByRequesterAndEvent(Long userId, Long id);

    List<Request> findAllByEvent(Long eventId);

    List<Request> findByIdInAndEvent(List<Long> requestIds, Long eventId);

    int countByEventAndStatus(Long eventId, RequestStatus status);
}
