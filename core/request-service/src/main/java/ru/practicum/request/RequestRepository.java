package ru.practicum.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.feign.request.enums.RequestStatus;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Page<Request> findAllByRequesterId(Long userId, Pageable pageable);

    boolean existsByRequesterIdAndEventId(Long userId, Long id);

    List<Request> findAllByEventId(Long eventId);

    List<Request> findByIdInAndEventId(List<Long> requestIds, Long eventId);

    int countByEventIdAndStatus(Long eventId, RequestStatus status);
}
